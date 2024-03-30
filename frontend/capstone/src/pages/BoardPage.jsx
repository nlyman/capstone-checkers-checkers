import { useState, useEffect } from 'react';
import BoardComponent from '../components/BoardComponent';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import NavBarComponent from '../components/NavBarComponent';
import { verifyLoginStatus } from '../functions/CookieFunctions';

const BoardPage = () => {


    const navigate = useNavigate();
    if (!verifyLoginStatus()){
        navigate("/");
    }

    let [board, setBoard] = useState({getSpaces: ()=>{return [];}});
    let [allowedMoves, setAllowedMoves] = useState([]);
    let [selectedSpace, setSelectedSpace] = useState({column: -1, row: -1});
    let [changes, setChanges] = useState([]);
    let [firstMove, setFirstMove] = useState(true);

    useEffect(() => {
        async function getRoom(){
            try{
                const response = await axios.get("/api/board/room");
                setBoard(BoardComponent(response.data));
            }catch (error){
                // console.log(error);
            }
        }
        getRoom();
    }, []);

    // useEffect(() => {
    //     console.log(board);
    //     console.log(board.getSpaces());
    // }, [board]);

    function selectSpace(column, row){
        if (selectedSpace.column == column && selectedSpace.row == row){
            deselectSpace();
            return;//Exits this if they reselected space.
        }
        resetSpace(column, row);
        const theAllowedMoves=board.getAllowedMoves(firstMove, column, row);
        console.log(theAllowedMoves);
        setAllowedMoves(theAllowedMoves);
        for(const move of theAllowedMoves){
            board.setSpace(5, move.column, move.row);
        }
    }

    function resetSpace(column, row){
        unsetAllowedMoves();
        setSelectedSpace({column: column, row: row});
    }

    function deselectSpace(){
        unsetAllowedMoves();
        setSelectedSpace({column: -1, row: -1});
        setAllowedMoves([]);
    }

    function unsetAllowedMoves(){
        for(const allowed of allowedMoves){
            board.setSpace(0, allowed.column, allowed.row);
        }
    }

    function moveTo(column, row){
        let move;
        for(const allowed of allowedMoves){
            if (allowed.column == column && allowed.row == row){
                move=allowed;
                break;
            }
        }
        if (move === 'undefined'){
            return;
        }
        setFirstMove(false);
        let selectedColumn=selectedSpace.column;
        let selectedRow=selectedSpace.row;
        deselectSpace();
        let movedPiece = board.getSpace(selectedColumn, selectedRow);

        addChange({old: movedPiece, new: 0, column:selectedColumn, row: selectedRow});

        //This is to promote the piece.
        if (movedPiece<3){
            if (column==0 || column==7){
                movedPiece+=2;
            }
        }

        addChange({old: 0, new: movedPiece, column: column, row: row});
        if (move.hopX){
            let replaced = board.getSpace(move.hopY, move.hopX);
            addChange({old: replaced, new: 0, column: move.hopY, row: move.hopX});
        }
    }

    function addChange(change){
        changes.push(change);
        console.log(change.new);
        board.setSpace(change.new, change.column, change.row);
    }

    function resetMove(){
        if (firstMove){
            return;
        }
        setFirstMove(true);
        for(let i=changes.length-1; i>=0; i--){
            board.setSpace(changes[i].old, changes[i].column, changes[i].row);
        }
        deselectSpace();
        setChanges([]);
    }

    function submitMove(){
        if (firstMove){
            return true;
        }
        try{
            submit();
            setFirstMove(true);
            deselectSpace();
            setChanges([]);
        }catch(error){
            console.log(error);
        }
    }

    async function submit(){
        let size=1;//Skip over starting spot.
        for(let i=1; i<changes.length; i++){
            if (i%3!=1){
                continue;
            }
            size++;
        }
        let columns=new Array(size);
        let rows=new Array(size);
        columns[0]=changes[0].column;
        rows[0]=changes[0].row;
        for(let i=1, size=1; i<changes.length; i++){
            if (i%3!=1){//The piece hopped over does not get put in the dto.
                continue;
            }
            columns[size]=changes[i].column;
            rows[size]=changes[i].row;
            size++;
        }

        board.convertColumns(columns);
        board.convertRows(rows);
        const moveData = { columns: columns, rows: rows };

        await axios.post('http://localhost:8080/api/board/move', moveData, {
            headers: { 'Accept': 'application/json'}
        });
    }

    async function leaveRoom(){
        await axios.delete("/api/board/leave");
        navigate("/");
    }

    function getClass(space){
        //king is a second class added.
        let name='viable';
        switch(space){
            case 0:
                name+=' empty';
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                name='highlighted';
                break;
            case 6:
                name='nonviable'
                break;
        }
        return name;
    }

    function getContentClass(space){
        switch(space){
            case 1:
                return 'red';
            case 2:
                return 'black';
            case 3:
                return 'red king';
            case 4:
                return 'black king';
            case 0:
            case 5:
            case 6:
                return '';
        }
    }

    function getClickEvent(space, column, row){
        switch(space){
            case 0:
            case 6:
                break;
            case 1:
            case 3:
                if (board.getIsPlayerOne()){selectSpace(column, row);}
                break;
            case 2:
            case 4:
                if (!board.getIsPlayerOne()){selectSpace(column, row);}
                break;
            case 5:
                moveTo(column, row);
                break;
        }
    }

    return(<>
        {NavBarComponent()}
        <table className="checkers">
            <tbody>
                {board.getSpaces().map((outer, column) => (
                    <tr key={column}>
                        {outer.map((space, row) =>(
                            <td
                                key={row}
                                className={getClass(space)}
                                onClick={() => getClickEvent(space, column, row)}
                            >
                                <div className={getContentClass(space)}>
                                    {/* {getContent(space)} */}
                                </div>
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
        <button onClick={() => resetMove()}>
            reset move
        </button>
        <button onClick={() => submitMove()}>
            submit move
        </button>
        <button onClick={() => leaveRoom()}>
            leave
        </button>
        </>
    );
}

export default BoardPage;