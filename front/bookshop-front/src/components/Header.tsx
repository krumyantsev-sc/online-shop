import React, {useEffect} from 'react';
import "../styles/Header.css"
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import AutoStoriesIcon from '@mui/icons-material/AutoStories';
import BookOnlineIcon from '@mui/icons-material/BookOnline';
import {useAuth} from './auth/context/AuthContextProvider';
import {Link} from "react-router-dom";

const Header = () => {
    const {isAuthenticated, logout} = useAuth();

    useEffect(() => {
        console.log(isAuthenticated);
        console.log()
    }, [])

    useEffect(() => {
        console.log(isAuthenticated);
    }, [isAuthenticated]);

    return (
        <div className={"header-container"}>
            <div className="menu-container">
                <div className="logo-container">
                    <Link to={"/"}>
                        <BookOnlineIcon className={"logo-icon"}/>
                        <span className={"logo-name"}>Books online</span>
                    </Link>
                </div>
                <div className="menu">
                    {isAuthenticated &&
                    <div className="menu-item-container">
                        <Link to={"/profile"}>
                            <PersonIcon fontSize={"large"}/>
                            <span className={"menu-item-name"}>Profile</span>
                        </Link>
                    </div>
                    }
                    <div className="menu-item-container">
                        <Link to={"/catalog"}>
                            <AutoStoriesIcon fontSize={"large"}/>
                            <span className={"menu-item-name"}>Catalog</span>
                        </Link>
                    </div>
                    {!isAuthenticated ?
                        <div className="menu-item-container">
                            <Link to={"/login"}>
                                <LoginIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>Log in</span>
                            </Link>
                        </div>
                        :
                        <div className="menu-item-container"
                             onClick={logout}
                        >
                            <Link to={"/"}>
                                <LogoutIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>Log out</span>
                            </Link>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
};

export default Header;