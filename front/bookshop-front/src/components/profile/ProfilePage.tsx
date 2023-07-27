import React, {useEffect, useState} from 'react';
import Header from "../Header";
import BookService from "../../API/BookService";
import ProfileService from "../../API/ProfileService";
import {UserInfo} from "./UserInfo";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import ProfileInfo from "./ProfileInfo";
import "../../styles/Profile.css"
import UserForm from "./UserForm";

const ProfilePage = () => {
    const [displayInfo, setDisplayInfo] = useState<boolean>(true);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [userInfo, setUserInfo] = useState<UserInfo>();
    const navigate = useNavigate();

    async function getProfileFromServer() {
        try {
            setIsLoading(true);
            const response = await ProfileService.getProfile();
            const data = await response.data;
            if (data) {
                setUserInfo(response.data);
                setIsLoading(false);
            }
        } catch (error) {
            console.error('Ошибка при получении данных:', error);
            navigate('/');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        getProfileFromServer()
    }, []);

    if (isLoading) {
        return <Loading/>;
    }

    return (
        <div>
            <Header/>
            <div className="profile-page">
                <div className="profile-container">
                        {userInfo && (displayInfo ? <ProfileInfo profileInfo={userInfo}/> : <UserForm initialEmail={userInfo.email}/>)}
                    <div
                        className="profile-switch-button"
                        onClick={() => {setDisplayInfo(!displayInfo)}}>
                        {displayInfo ? "CHANGE CREDENTIALS" : "VIEW PROFILE"}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;