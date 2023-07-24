import React from 'react';
import IBook from "./IBook";
import "../../styles/Catalog.css"
import cardImgPlaceholder from "../../assets/img/question.jpg"
import SettingsIcon from '@mui/icons-material/Settings';
import {useAuth} from "../auth/context/AuthContextProvider";
import {FileUploadDialog} from "./FileUploadDialog";
import BookModal from "./EditBookModal";

interface CardBookProps extends IBook {
    getBooksFromServer: () => {};
}

const Card: React.FC<CardBookProps> = ({
                                           title,
                                           author,
                                           genre,
                                           price,
                                           uuid,
                                           getBooksFromServer
                                       }) => {
    const {isAdmin} = useAuth();
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    return (
        <div className={"card"}>
            <img src={cardImgPlaceholder} alt="bookImage"/>
            <div className="desc-container">
                <span>Название: {title}</span><br/>
                <span>Автор: {author}</span><br/>
                <span>Жанр: {genre}</span><br/>
            </div>
            <div className="card-buttons-container">
                <div className="download-button">
                    СКАЧАТЬ
                </div>
                {isAdmin && <div className="edit-button">
                    <SettingsIcon
                        onClick={handleClickOpen}
                    />
                </div>}
            </div>
            <BookModal
                open={open}
                handleClose={handleClose}
                getBooksFromServer={getBooksFromServer}
                author={author}
                genre={genre}
                title={title}
                uuid={uuid}
            />
        </div>
    );
};

export default Card;