import React from 'react';
import Header from "../Header";
import UserSupportPage from "./UserSupportPage";
import styles from "../../styles/SupportPage.module.css"

const SupportPage = () => {
    return (
        <div>
            <Header/>
            <div className={styles.chatsContainer}>
                <UserSupportPage/>
            </div>
        </div>
    );
};

export default SupportPage;