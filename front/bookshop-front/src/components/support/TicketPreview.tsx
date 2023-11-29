import React from 'react';
import ITicket from "./ITicket";
import {Avatar} from "@mui/material";
import styles from "../../styles/TicketPreview.module.css"
import {TicketStatus} from "../../enums/TicketStatus";
import TicketService from "../../API/TicketService";

interface TicketPreviewProps extends ITicket {
    fetchTickets: () => {};
}

const TicketPreview: React.FC<TicketPreviewProps> = ({
                                                         username,
                                                         title,
                                                         lastMessage,
                                                         uuid,
                                                         isRead,
                                                         timestamp,
                                                         setActiveChat,
                                                         status,
                                                         fetchTickets
                                                     }) => {

    const handlePreviewClick = () => {
        setActiveChat(uuid);
        TicketService.readTicket(uuid).then(fetchTickets);
    }

    return (
        <div
            className={styles.ticketPreviewContainer}
            onClick={handlePreviewClick}
        >
            <div className={styles.credsContainer}>
                <Avatar>
                    {username.at(0)}
                </Avatar>
                {username}
            </div>
            <div className={styles.ticketInfoContainer}>
                <div className={styles.titleTimestampContainer}>
                    <span className={styles.title}>{title}</span>
                    <div className={styles.dotTime}>
                        <span className={styles.timestamp}>{timestamp}</span>
                        {!isRead &&
                        <div className={styles.glowingDot}>
                        </div>}
                        {status == TicketStatus.Closed &&
                        <div className={styles.statusContainer}>
                            {status}
                        </div>}
                    </div>
                </div>
                <div className={styles.previewMessageContainer}>
                    {lastMessage}
                </div>
            </div>
        </div>
    );
};

export default TicketPreview