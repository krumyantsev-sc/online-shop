import React from 'react';
import IMessage from "./IMessage";
import {Avatar} from "@mui/material";
import styles from "../../styles/Chat.module.css"

const Message: React.FC<IMessage> = ({username, content, timestamp}) => {
    return (
        <div className={styles.chatMessageContainer}>
            <div className={styles.messageAvatarNameContainer}>
                <Avatar sx={{width: 50, height: 50, backgroundColor: "lightblue"}}>
                    {username.at(0)}
                </Avatar>
                {username}
            </div>
            <div className={styles.messageTimeContentContainer}>
                <div className={styles.messageTimeContainer}>
                    {timestamp}
                </div>
                <div className={styles.messageContentContainer}>
                    {content}
                </div>
            </div>
        </div>
    );
};

export default Message;