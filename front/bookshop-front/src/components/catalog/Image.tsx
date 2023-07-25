import React, { useState, useEffect } from 'react';
import BookService from "../../API/BookService";
import cardImgPlaceholder from "../../assets/img/question.jpg"

interface ImageProps {
    uuid: string;
}

const ImageComponent: React.FC<ImageProps> = ({uuid}) => {
    const [imageData, setImageData] = useState<string | null>(null);

    useEffect(() => {
        const fetchImageData = async () => {
            try {
                const response = await BookService.getCover(uuid);
                const base64 = btoa(new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), ''));
                const imageSrc = `data:image/png;base64,${base64}`;
                setImageData(imageSrc);
            } catch (error) {
                setImageData(cardImgPlaceholder);
                console.error('Ошибка при загрузке изображения:', error);
            }
        };

        fetchImageData();
    }, []);

    return (
        <div>
            {imageData ? (
                <img src={imageData} alt="book cover" />
            ) : (
                <span>Загрузка изображения...</span>
            )}
        </div>
    );
};

export default ImageComponent;