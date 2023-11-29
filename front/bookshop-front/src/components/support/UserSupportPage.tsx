import React, {useEffect, useState} from 'react';
import styles from "../../styles/UserSupportPage.module.css"
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import TicketModal from "./TicketModal";
import ITicket from "./ITicket";
import TicketService from "../../API/TicketService";
import TicketPreview from "./TicketPreview";
import Chat from "./Chat";
import {useAuth} from "../auth/context/AuthContextProvider";
import {Roles} from "../../enums/Roles";
import ClearIcon from '@mui/icons-material/Clear';
import {io} from "socket.io-client";
import {useSocket} from "../socket/SocketContext";

const UserSupportPage = () => {
    const socket = useSocket();
    const [isModalOpen, setModalOpen] = useState(false);
    const [tickets, setTickets] = useState<ITicket[]>([]);
    const [activeChat, setActiveChat] = useState<string | null>(null);
    const {roles} = useAuth();

    const newMessageHandler = () => {
        fetchTickets()
    }

    useEffect(() => {
        if (socket) {
            socket.on('newMessage', newMessageHandler);

            return () => {
                socket.off('newMessage');
            };
        }
    }, [socket]);

    const fetchTickets = async () => {
        const response = roles.includes(Roles.Admin) ?
            await TicketService.getTickets()
            :
            await TicketService.getUserTickets();
        const data = await response.data;
        if (data) {
            setTickets(data);
        }
    }

    const handleAddClick = () => {
        setModalOpen(true);

    }

    const handleCloseClick = () => {
        TicketService.closeTicket(activeChat!).then(() => {
                setActiveChat(null);
                fetchTickets();
            }
        )
    }

    useEffect(() => {
        fetchTickets();
    }, [activeChat])

    useEffect(() => {
        fetchTickets();
    }, [])

    return (
        <div className={styles.chats}>
            <div className={styles.ticketsContainer}>
                <div className={styles.labelContainer}>
                    TICKETS
                </div>
                {tickets.map((ticket) =>
                    <TicketPreview
                        username={ticket.username}
                        title={ticket.title}
                        lastMessage={ticket.lastMessage}
                        uuid={ticket.uuid}
                        timestamp={ticket.timestamp}
                        isRead={ticket.isRead}
                        status={ticket.status}
                        setActiveChat={setActiveChat}
                        fetchTickets={fetchTickets}
                    />
                )}
            </div>
            <div className={styles.chatContainer}>
                <div className={styles.labelContainer}>
                    CONVERSATION
                </div>
                <Chat activeChat={activeChat}/>
            </div>
            <div className={styles.controlsContainer}>
                <AddCircleOutlineIcon
                    style={{fontSize: 50}}
                    onClick={handleAddClick}
                />
                <ClearIcon
                    style={{fontSize: 50}}
                    onClick={handleCloseClick}
                />

            </div>
            <TicketModal open={isModalOpen} onClose={() => {
                setModalOpen(false);
                fetchTickets();
            }}/>
        </div>
    );
};

export default UserSupportPage;