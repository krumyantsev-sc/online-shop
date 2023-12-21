import React, {useEffect, useState} from 'react';
import styles from "../../styles/Chat.module.css"
import TicketService from "../../API/TicketService";
import Message from "./Message";
import IMessage from "./IMessage";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {useSocket} from "../socket/SocketContext";

interface ChatProps {
    activeChat: string | null;
}

const Chat: React.FC<ChatProps> = ({activeChat}) => {
    const socket = useSocket();
    const [messages, setMessages] = useState<IMessage[]>([]);
    const [message, setMessage] = useState('');

    const newMessageHandler = (data: { uuid: string }) => {
        if (data.uuid === activeChat) {
            fetchMessages();
        }
    }

    useEffect(() => {
        if (socket) {
            socket.on('newMessage', newMessageHandler);

            return () => {
                socket.off('newMessage');
            };
        }
    }, [socket]);

    const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setMessage(event.target.value);
    };

    const handleSubmit = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        TicketService.sendMessage({message, uuid: activeChat!}).then(() => {
            setMessage('');
            fetchMessages();
        });
    };

    useEffect(() => {
        activeChat &&
        fetchMessages()
    }, [activeChat])

    const fetchMessages = async () => {
        const response = await TicketService.getMessages(activeChat!);
        const data = await response.data;
        if (data) {
            setMessages(data);
        }
    }

    return (
        <div className={styles.chatContainer}>
            <div className={styles.chatMessagesContainer}>
                {messages && messages.map((msg) =>
                    <Message
                        username={msg.username}
                        content={msg.content}
                        timestamp={msg.timestamp}/>
                )}
            </div>
            <div className={styles.chatControlsContainerWrapper}>
                {activeChat &&
                <div className={styles.chatControlsContainer}>
                    <TextField
                        label="Введите сообщение"
                        variant="outlined"
                        value={message}
                        onChange={handleMessageChange}
                        fullWidth
                        margin="normal"
                        style={{width: "20rem"}}
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSubmit}
                    >
                        Отправить
                    </Button>
                </div>
                }
            </div>
        </div>
    );
};

export default Chat;