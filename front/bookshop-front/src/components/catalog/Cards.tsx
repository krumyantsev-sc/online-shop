import React, {useEffect, useState} from 'react';
import BookService from "../../API/BookService";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import IBook from "./IBook";
import Card from "./Card";
import "../../styles/Catalog.css"
import AddCard from "./AddCard";
import {useAuth} from "../auth/context/AuthContextProvider";


const Cards = () => {
    const {isAdmin} = useAuth();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [books, setBooks] = useState<IBook[]>([]);
    const navigate = useNavigate();

    async function getBooksFromServer() {
        try {
            const response = await BookService.getBooks();
            const data = await response.data;
            if (data) {
                setBooks(data);
                setIsLoading(false);
            }
        } catch (error) {
            console.error('Ошибка при получении игр:', error);
            navigate('/');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        getBooksFromServer();
    }, []);

    if (isLoading) {
        return <Loading/>;
    }

    return (
        <div className={"cards-container-wrapper"}>
            <div className={"cards-container"}>
                {books.map((book) => (
                    <Card
                        key={book.uuid}
                        author={book.author}
                        title={book.title}
                        genre={book.genre}
                        price={book.price}
                        uuid={book.uuid}
                        getBooksFromServer={getBooksFromServer}
                    />
                ))}
                {isAdmin &&
                <AddCard getBooksFromServer={getBooksFromServer}/>}
            </div>
        </div>
    );
};

export default Cards;