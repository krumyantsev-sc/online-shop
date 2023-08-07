import React, {useEffect, useState} from 'react';
import {IComment} from "./IComment";
import "../../../styles/BookPage.css"
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import {useAuth} from "../../auth/context/AuthContextProvider";
import {Roles} from "../../../enums/Roles";
import CommentService from "../../../API/CommentService";
import CommentModal from "./CommentModal";

interface CommentProps extends IComment {
    getComments: () => {};
}

const Comment: React.FC<CommentProps> = ({username, text, timestamp, uuid, getComments}) => {
    const {roles: userRoles, username: userLogin} = useAuth();
    const [openUpdate, setOpenUpdate] = useState(false);

    const handleDelete = () => {
        CommentService.deleteComment(uuid).then(getComments);
    }

    const handleUpdate = (comment: string) => {
        CommentService.updateComment(uuid, comment).then(getComments);
    };

    const handleClose = () => {
        setOpenUpdate(false);
    };

    const handleClickOpenUpdate = () => {
        setOpenUpdate(true);
    };


    return (
        <div className={"comment-container"}>
            <div className="comment-credentials">
                {username} <br/>
                {timestamp}
            </div>
            <div className="comment-text">
                {text}
            </div>

            <div className="comment-controls">
                {(userLogin === username || userRoles.includes(Roles.Admin)) &&
                <>
                    <EditIcon
                        onClick={handleClickOpenUpdate}
                    />
                    <DeleteIcon
                        onClick={handleDelete}
                    />
                </>
                }
            </div>
            <CommentModal onSave={handleUpdate} defaultComment={text} open={openUpdate}
                          handleClose={handleClose}/>
        </div>
    );
};

export default Comment;