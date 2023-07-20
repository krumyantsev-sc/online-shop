import React from 'react';
import IBook from "./IBook";

const Card: React.FC<IBook> = ({
    title,
    author,
    genre,
    price,
    uuid
}) => {
    return (
        <div className={"card"}>
            {title}
            {author}
            {genre}
            {price}
            {uuid}
        </div>
    );
};

export default Card;