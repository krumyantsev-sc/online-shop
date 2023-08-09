import React, {useState} from 'react';
import {TextField, Button, containerClasses} from '@mui/material';
import "../../styles/Profile.css"
import ProfileService from "../../API/ProfileService";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/context/AuthContextProvider";
import {useTranslation} from "react-i18next";

interface Props {
    initialEmail: string;
}

const UserForm: React.FC<Props> = ({initialEmail}) => {
    const {t} = useTranslation();
    const [email, setEmail] = useState(initialEmail);
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const nav = useNavigate();
    const {logout} = useAuth();
    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(event.target.value);
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(event.target.value);
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (password !== confirmPassword) {
            setErrorMessage(t('confirmPassError'));
        } else {
            setErrorMessage("");
            try {
                ProfileService.updateCredentials({email, password})
                    .then(() => {
                        logout();
                        nav("/login");
                    });
            } catch (e) {
                console.log(e);
            }

        }
    };

    return (

        <form className={"user-form"} onSubmit={handleSubmit}>
            <div className={"user-form-fields-container"}>
                <TextField
                    className={"user-from-input"}
                    label={t('email')}
                    value={email}
                    onChange={handleEmailChange}
                    type="email"
                />
                <TextField
                    label={t('password')}
                    value={password}
                    onChange={handlePasswordChange}
                    type="password"
                />
                <TextField
                    label={t('confirmPass')}
                    value={confirmPassword}
                    onChange={handleConfirmPasswordChange}
                    type="password"
                    error={!!errorMessage}
                    helperText={errorMessage}
                />
                <Button type="submit">{t('submit')}</Button>
            </div>
        </form>
    );
};

export default UserForm;