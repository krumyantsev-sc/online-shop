import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@mui/material";
import * as React from 'react';
import {useRef} from "react";
import BookService from "../../API/BookService";

interface FileUploadDialogProps {
    open: boolean;
    handleClose: () => void;
    getBooksFromServer: () => {};
}

export const FileUploadDialog: React.FC<FileUploadDialogProps> = ({open, handleClose, getBooksFromServer}) => {
    const formRef = useRef<HTMLFormElement>(null);

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        if (formRef.current) {
            const formData = new FormData();
            const fileField = formRef.current.elements.namedItem('file') as HTMLInputElement;
            if (fileField.files) {
                formData.append('file', fileField.files[0]);
            }
            BookService.uploadBook(formData).then(getBooksFromServer);
        }
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <form onSubmit={handleSubmit} ref={formRef}>
                <DialogTitle>Upload a File</DialogTitle>
                <DialogContent>
                    <TextField
                        variant="outlined"
                        type="file"
                        inputProps={{accept: '.pdf,.epub'}}
                        name="file"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button type="submit">Upload</Button>
                </DialogActions>
            </form>
        </Dialog>
    );
};