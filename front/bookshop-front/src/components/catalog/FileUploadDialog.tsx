import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@mui/material";
import * as React from 'react';
import {useRef} from "react";
import BookService from "../../API/BookService";
import {useTranslation} from "react-i18next";

interface FileUploadDialogProps {
    open: boolean;
    handleClose: () => void;
    getBooksFromServer: () => {};
}

export const FileUploadDialog: React.FC<FileUploadDialogProps> = ({open, handleClose, getBooksFromServer}) => {
    const formRef = useRef<HTMLFormElement>(null);
    const {t: i18n} = useTranslation();
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
                <DialogTitle>{i18n("uploadDialogTitle")}</DialogTitle>
                <DialogContent>
                    <TextField
                        variant="outlined"
                        type="file"
                        inputProps={{accept: '.pdf,.epub'}}
                        name="file"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>{i18n('cancel')}</Button>
                    <Button type="submit">{i18n('upload')}</Button>
                </DialogActions>
            </form>
        </Dialog>
    );
};