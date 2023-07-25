import React, {useEffect, useState} from 'react';
import BookService from "../../API/BookService";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import IBook from "./IBook";
import Card from "./Card";
import "../../styles/Catalog.css"
import AddCard from "./AddCard";
import {useAuth} from "../auth/context/AuthContextProvider";
import {Roles} from "../../enums/Roles";
import SortMenu from "./SortMenu";


const Cards = () => {
    const {roles} = useAuth();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [books, setBooks] = useState<IBook[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [pageSize, setPageSize] = useState<number>(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const navigate = useNavigate();
    const [sortField, setSortField] = useState<string>("id");
    const [sortDirection, setSortDirection] = useState<string>("ASC");

    const handleSortChange = (value: string) => {
        const [field, direction] = value.split(':');
        console.log(field, direction);
        console.log(currentPage);
        setSortField(field);
        setSortDirection(direction);
        getBooksFromServer(currentPage, pageSize, field, direction);
    };


    async function getBooksFromServer(page: number, size: number, field: string = "id", direction: string = "ASC") {
        try {
            setIsLoading(true);
            const response = await BookService.getBooks(page - 1, size, field, direction);
            const data = await response.data;
            console.log(response.data.books)
            if (data) {
                setBooks(response.data.books);
                setTotalPages(response.data.totalPages);
                setIsLoading(false);
            }
        } catch (error) {
            console.error('Ошибка при получении книг:', error);
            navigate('/');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        getBooksFromServer(currentPage, pageSize, "id", "ASC");
    }, [currentPage, pageSize]);

    if (isLoading) {
        return <Loading/>;
    }

    return (
        <div className={"cards-container-wrapper"}>
            <div className="cards-sort-container">
                <SortMenu sortField={sortField} sortDirection={sortDirection} onSortChange={handleSortChange}/>
                <div className={"cards-container"}>
                    {books.map((book) => (
                        <Card
                            key={book.uuid}
                            author={book.author}
                            title={book.title}
                            genre={book.genre}
                            price={book.price}
                            uuid={book.uuid}
                            getBooksFromServer={() => getBooksFromServer(currentPage, pageSize)}
                        />
                    ))}
                    {roles.includes(Roles.Admin) &&
                    <AddCard getBooksFromServer={() => getBooksFromServer(currentPage, pageSize)}/>}
                </div>
            </div>
            <div className="pagination">
                <button
                    disabled={currentPage === 1}
                    onClick={() => setCurrentPage(currentPage - 1)}
                >
                    Previous
                </button>
                <span>{currentPage} / {totalPages}</span>
                <button
                    disabled={currentPage === totalPages}
                    onClick={() => setCurrentPage(currentPage + 1)}
                >
                    Next
                </button>
            </div>
        </div>
    );
};

export default Cards;