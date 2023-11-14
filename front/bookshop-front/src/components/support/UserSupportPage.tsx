import React from 'react';
import styles from "../../styles/UserSupportPage.module.css"
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

const UserSupportPage = () => {
    return (
        <div className={styles.chats}>
            <div className={styles.ticketsContainer}>
                <div className={styles.labelContainer}>
                    TICKETS
                </div>
            </div>
            <div className={styles.chatContainer}>
                <div className={styles.labelContainer}>
                    CONVERSATION
                </div>
            </div>
            <div className={styles.controlsContainer}>
                <AddCircleOutlineIcon style={{fontSize: 50}}/>
            </div>
        </div>
    );
};

export default UserSupportPage;