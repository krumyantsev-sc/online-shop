import React, {useState} from 'react';
import {Button, Modal, Box, TextField} from '@mui/material';
import TicketService from "../../API/TicketService";

interface TicketModalProps {
    open: boolean;
    onClose: () => void;
}

const TicketModal: React.FC<TicketModalProps> = ({open, onClose}) => {
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = () => {
        TicketService.createTicket({message, title})
        onClose();
    };

    const modalStyle = {
        position: 'absolute' as const,
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        boxShadow: 24,
        p: 4,
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={modalStyle}>
                <TextField
                    label="Тема"
                    fullWidth
                    margin="normal"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
                <TextField
                    label="Сообщение"
                    fullWidth
                    margin="normal"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                />
                <Button onClick={handleSubmit} variant="contained" color="primary">
                    СОЗДАТЬ
                </Button>
            </Box>
        </Modal>
    );
};

export default TicketModal;