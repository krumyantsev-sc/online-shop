import React from 'react';
import ITicket from "./ITicket";
import {Avatar} from "@mui/material";
import styles from "../../styles/TicketPreview.module.css"

const TicketPreview: React.FC<ITicket> = ({
                                              username,
                                              title,
                                              lastMessage,
                                              uuid,
                                              isRead,
                                              timestamp,
                                              setActiveChat
                                          }) => {

    return (
        <div
            className={styles.ticketPreviewContainer}
            onClick={() => {setActiveChat(uuid)}}
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
                    </div>
                </div>
                {lastMessage}
            </div>
        </div>
    );
};

export default TicketPreview