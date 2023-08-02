import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import BookService from "../../API/BookService";
import IBook from "../catalog/IBook";
import Card from "../catalog/Card";
import "../../styles/BookPage.css"
import Header from "../Header";

const BookPage = () => {
    let {bookUuid} = useParams();
    const [bookInfo, setBookInfo] = useState<IBook>();

    useEffect(() => {
        getBookInfoFromServer();
    }, [])

    const getBookInfoFromServer = async () => {
        try {
            let response = await BookService.getBook(bookUuid!);
            let data = await response.data;
            setBookInfo(data);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div>
            <Header/>
            <div className={"book-page"}>
                {bookInfo &&
                <div className={"card-desc-container"}>
                    <Card
                        getBooksFromServer={getBookInfoFromServer}
                        title={bookInfo.title}
                        genre={bookInfo.genre}
                        author={bookInfo.author}
                        uuid={bookInfo.uuid}
                        description={bookInfo.description}
                    />
                    <div className="book-page-desc-container">
                        <div className={"desc"}>
                            {bookInfo.description ? bookInfo.description : "NO DESCRIPTION"}
                        </div>
                    </div>
                </div>
                }
            </div>
        </div>
    );
};

export default BookPage;