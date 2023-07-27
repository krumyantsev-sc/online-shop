import React from 'react';
import {UserInfo} from "./UserInfo";
import {Avatar} from "@mui/material";
import "../../styles/Profile.css"

interface ProfileInfoProps {
    profileInfo: UserInfo;
}

const ProfileInfo: React.FC<ProfileInfoProps> = ({profileInfo}) => {
    return (
        <div className={"info-container"}>
            <div className="avatar-container">
                <Avatar
                    sx={{ width: 130, height: 130 }}
                />
            </div>
            <div className="credentials">
                <div className={"credentials-info"}>
                    <b>Username:</b> <br/>
                    {profileInfo.username}<br/>
                </div>
                <div className={"credentials-info"}>
                    <b> E-mail:</b> <br/>
                    {profileInfo.email}<br/>
                </div>
                <div className={"credentials-info"}>
                    <b>Registration date: </b> <br/>
                    {profileInfo.regDate}<br/>
                </div>
            </div>
        </div>
    );
};

export default ProfileInfo;