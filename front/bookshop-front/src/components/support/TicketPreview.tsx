import React from 'react';
import ITicket from "./ITicket";
import {Avatar} from "@mui/material";
import styles from "../../styles/TicketPreview.module.css"
import {TicketStatus} from "../../enums/TicketStatus";

const TicketPreview: React.FC<ITicket> = ({
                                              username,
                                              title,
                                              lastMessage,
                                              uuid,
                                              isRead,
                                              timestamp,
                                              setActiveChat,
                                              status
                                          }) => {

    return (
        <div
            className={styles.ticketPreviewContainer}
            onClick={() => {
                setActiveChat(uuid)
            }}
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
                        {isRead &&
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