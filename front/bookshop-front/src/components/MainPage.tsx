import React from 'react';
import Header from "./Header";
import "../styles/Main.css"
import bookLogo from "../assets/img/book-gif.png"
import binocularLogo from "../assets/img/Binoculars.png"

const MainPage = () => {
    return (
        <div>
            <Header/>
            <div className="main-page-wrapper">
                <div className="main-page-container">
                    <div className="what-book-container">
                        <div className="what-book-sign">
                            What book you looking for?<img src={bookLogo}/>
                        </div>
                        <div className="explore-catalog-sign">
                            Explore our catalog and find your next read.
                        </div>
                        <div className="main-button-line-container">
                            <div className="main-page-catalog-button">
                                Explore <img src={binocularLogo}/>
                            </div>
                            <div className="dashed-line"/>
                        </div>
                    </div>
                    <div className="main-img-wrapper">
                        <div className="main-img-container">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainPage;