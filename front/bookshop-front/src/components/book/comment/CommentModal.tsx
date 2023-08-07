import * as React from 'react';
import { Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

interface MyDialogProps {
    onSave: (comment: string) => void;
    defaultComment: string;
    open: boolean;
    handleClose: () => void;
}

const CommentModal: React.FC<MyDialogProps> = ({ onSave, defaultComment, open, handleClose }) => {
    const [comment, setComment] = React.useState<string>(defaultComment);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setComment(event.target.value);
    };

    const handleSave = () => {
        onSave(comment);
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Введите комментарий</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    label="Комментарий"
                    type="text"
                    fullWidth
                    value={comment}
                    onChange={handleChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Отмена</Button>
                <Button onClick={handleSave}>Сохранить</Button>
            </DialogActions>
        </Dialog>
    );
};

export default CommentModal;