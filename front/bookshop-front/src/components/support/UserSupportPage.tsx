import React, {useEffect, useState} from 'react';
import styles from "../../styles/UserSupportPage.module.css"
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import TicketModal from "./TicketModal";
import ITicket from "./ITicket";
import TicketService from "../../API/TicketService";
import TicketPreview from "./TicketPreview";
import Chat from "./Chat";

const UserSupportPage = () => {
    const [isModalOpen, setModalOpen] = useState(false);
    const [tickets, setTickets] = useState<ITicket[]>([]);
    const [activeChat, setActiveChat] = useState<string | null>(null);

    const fetchTickets = async () => {
        const response = await TicketService.getTickets();
        const data = await response.data;
        if (data) {
            setTickets(data);
        }
    }

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
                        setActiveChat={setActiveChat}
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
                    onClick={() => setModalOpen(true)}
                />
            </div>
            <TicketModal open={isModalOpen} onClose={() => setModalOpen(false)}/>
        </div>
    );
};

export default UserSupportPage;