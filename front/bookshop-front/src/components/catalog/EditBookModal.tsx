import React, {useState, useEffect} from 'react';
import {Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button} from '@mui/material';
import BookService from "../../API/BookService";

interface BookModalProps {
    open: boolean;
    handleClose: () => void;
    title: string;
    genre: string;
    author: string;
    uuid: string;
    description: string
    getBooksFromServer: () => {};
}

const BookModal: React.FC<BookModalProps> = ({
                                                 open,
                                                 handleClose,
                                                 title,
                                                 genre,
                                                 author,
                                                 uuid,
                                                 getBooksFromServer,
                                                 description
                                             }) => {
    const [bookTitle, setBookTitle] = useState(title);
    const [bookGenre, setBookGenre] = useState(genre);
    const [bookAuthor, setBookAuthor] = useState(author);
    const [value, setValue] = React.useState(description);

    useEffect(() => {
        setBookTitle(title);
        setBookGenre(genre);
        setBookAuthor(author);
    }, [title, genre, author]);

    const validateInput = (input: string) => {
        return /^[A-Za-zА-Яа-я\s]+$/.test(input);
    };

    const handleSaveClick = () => {
        if (validateInput(bookTitle) && validateInput(bookGenre) && validateInput(bookAuthor)) {
            BookService.updateBook({title: bookTitle, genre: bookGenre, author: bookAuthor, description: value}, uuid)
                .then(getBooksFromServer);
            handleClose();
        } else {
            alert("Invalid input. Only English and Russian characters are allowed.");
        }
    };

    function handleDelete() {
        BookService.deleteBook(uuid).then(getBooksFromServer);
    }

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Edit Book</DialogTitle>
            <DialogContent>
                <TextField
                    value={bookTitle}
                    onChange={e => setBookTitle(e.target.value)}
                    label="Title"
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    value={bookGenre}
                    onChange={e => setBookGenre(e.target.value)}
                    label="Genre"
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    value={bookAuthor}
                    onChange={e => setBookAuthor(e.target.value)}
                    label="Author"
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    autoFocus
                    margin="dense"
                    id="description"
                    label="Description"
                    type="text"
                    fullWidth
                    multiline
                    value={value}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setValue(e.target.value)}
                />
            </DialogContent>
            <DialogActions>
                <Button color="primary" onClick={handleSaveClick}>Save</Button>
                <Button color="secondary" onClick={handleDelete}>Delete</Button>
            </DialogActions>
        </Dialog>
    );
};

export default BookModal;