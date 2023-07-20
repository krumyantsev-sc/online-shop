import React, {useEffect, useState} from 'react';
import BookService from "../../API/BookService";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import IBook from "./IBook";
import Card from "./Card";



const Cards = () => {
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
        <div>
            {books.map((book) => (
                <Card
                    key={book.uuid}
                    author={book.author}
                    title={book.title}
                    genre={book.genre}
                    price={book.price}
                    uuid={book.uuid}
                />
            ))}
        </div>
    );
};

export default Cards;