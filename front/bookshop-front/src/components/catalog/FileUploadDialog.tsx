import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@mui/material";
import * as React from 'react';
import {useRef, useState} from "react";
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
    const [price, setPrice] = useState<string>("");
    const [isPriceValid, setIsPriceValid] = useState<boolean>(true);

    const validatePrice = (value: string) => {
        const parsedValue = parseFloat(value);
        if (isNaN(parsedValue) || parsedValue < 0) {
            setIsPriceValid(false);
        } else {
            setIsPriceValid(true);
        }
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        if (!isPriceValid) return;
        if (formRef.current) {
            const formData = new FormData();
            const fileField = formRef.current.elements.namedItem('file') as HTMLInputElement;
            if (fileField.files) {
                formData.append('file', fileField.files[0]);
            }
            formData.append('price', price);
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
                    <TextField
                        variant="outlined"
                        label={i18n("price")}
                        value={price}
                        onChange={(e) => {
                            const value = e.target.value;
                            setPrice(value);
                            validatePrice(value);
                        }}
                        error={!isPriceValid}
                        helperText={isPriceValid ? "" : i18n("priceValidationError")}
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