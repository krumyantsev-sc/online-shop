import React from 'react';
import IBook from "./IBook";
import "../../styles/Catalog.css"
import SettingsIcon from '@mui/icons-material/Settings';
import {useAuth} from "../auth/context/AuthContextProvider";
import BookModal from "./EditBookModal";
import {Roles} from "../../enums/Roles";
import ImageComponent from "./Image";
import PreviewModal from "./PreviewModal";
import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from 'react-i18next';

interface CardBookProps extends IBook {
    getBooksFromServer: () => {};
}

const Card: React.FC<CardBookProps> = ({
                                           title,
                                           author,
                                           genre,
                                           uuid,
                                           getBooksFromServer,
                                           description
                                       }) => {
    const {t: i18n} = useTranslation();
    const {roles} = useAuth();
    const [openEdit, setOpenEdit] = React.useState(false);
    const [openPreview, setOpenPreview] = React.useState(false);
    const navigate = useNavigate();
    let {bookUuid} = useParams();
    const handleClickOpenEdit = () => {
        setOpenEdit(true);
    };

    const handleCloseEdit = () => {
        setOpenEdit(false);
    };

    const handleClickOpenPreview = () => {
        setOpenPreview(true);
    };

    const handleClosePreview = () => {
        setOpenPreview(false);
    };

    function downloadFile(url: string, filename: string) {
        let link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    return (
        <div className={"card"}>
            <div
                className="img-container"
                onClick={handleClickOpenPreview}
                style={{cursor: "pointer"}}
            >
                <ImageComponent
                    uuid={uuid}
                />
            </div>
            <div className="desc-container"
                 onClick={() => {
                     navigate(`/catalog/${uuid}`)
                 }}
            >
                <span>{i18n('title')}: {title}</span><br/>
                <span>{i18n('author')}: {author}</span><br/>
                <span>{i18n('genre')}: {genre}</span><br/>
            </div>
            {bookUuid &&
            <div className="card-buttons-container">
                <div className="download-button"
                     onClick={() => {
                         downloadFile(`${process.env.REACT_APP_API_URL}/books/${uuid}/download`, title)
                     }}>
                    {i18n('download')}
                </div>
                {roles.includes(Roles.Admin) && <div className="edit-button">
                    <SettingsIcon
                        onClick={handleClickOpenEdit}
                    />
                </div>}
            </div>
            }
            <BookModal
                open={openEdit}
                handleClose={handleCloseEdit}
                getBooksFromServer={getBooksFromServer}
                author={author}
                genre={genre}
                title={title}
                uuid={uuid}
                description={description}
            />
            <PreviewModal open={openPreview} handleClose={handleClosePreview} uuid={uuid}/>
        </div>
    );
};

export default Card;