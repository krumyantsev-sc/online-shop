import React from 'react';
import IMessage from "./IMessage";
import {Avatar} from "@mui/material";
import styles from "../../styles/Chat.module.css"

const Message: React.FC<IMessage> = ({username, content, timestamp}) => {
    return (
        <div className={styles.chatMessageContainer}>
            <Avatar>
                {username.at(0)}
            </Avatar>
            {username}
            {content}
            {timestamp}
        </div>
    );
};

export default Message;