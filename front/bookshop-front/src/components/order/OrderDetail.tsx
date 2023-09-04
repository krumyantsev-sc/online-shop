import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import IBook from "../catalog/IBook";
import BookService from "../../API/BookService";
import "../../styles/Order.css"
import ImageComponent from "../catalog/Image";

interface OrderDetailProps {
    uuid: string,
    unitPrice: number,
    updateTotalPrice: (bookPrice: number) => void
}

const OrderDetail: React.FC<OrderDetailProps> = ({uuid, unitPrice, updateTotalPrice}) => {
    const [bookInfo, setBookInfo] = useState<IBook>();
    const quantity = 1;

    const getBookInfo = async () => {
        const response = await BookService.getBook(uuid);
        const data = await response.data;

        setBookInfo(data);
        if(data?.price) {
            updateTotalPrice(data.price);
        }
    }

    useEffect(() => {
        getBookInfo();
    }, []);
    return (
        <div className={"detail-container"}>
            <div className="book-name-order">
                <div className="order-detail-image-container">
                    {bookInfo?.uuid &&
                    <ImageComponent uuid={bookInfo.uuid}/>}
                </div>
                {bookInfo?.title} <br/>
                {bookInfo?.author}
            </div>
            <div>{quantity}</div>
            <div>{unitPrice ? unitPrice : bookInfo?.price}</div>
        </div>
    );
};

export default OrderDetail;