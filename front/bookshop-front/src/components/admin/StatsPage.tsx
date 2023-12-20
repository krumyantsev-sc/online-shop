import React, {useEffect, useState} from 'react';
import Header from "../Header";
import {SaleData} from "./ISaleData";
import OrderService from "../../API/OrderService";
import SalesChart from "./SalesChart";
import "../../styles/Admin.css"

const StatsPage = () => {
    const [amountStats, setAmountStats] = useState<SaleData[]>();
    const [selectedValue, setSelectedValue] = useState('option1');

    const fetchSalesData = async () => {
        const response = await OrderService.getOrderAmountStats(30);
        const data = await response.data;
        if (data) {
            setAmountStats(data);
        }
    }

    const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedValue(event.target.value);
    };

    useEffect(() => {
        fetchSalesData();
    }, []);

    return (
        <div>
            <Header/>
            {amountStats &&
            <div className={"chart-select-container"}>
                <SalesChart data={amountStats} displayAmount={selectedValue === "option1"}/>
                <select value={selectedValue} onChange={handleChange}>
                    <option value="option1">Amount</option>
                    <option value="option2">Sum</option>
                </select>
            </div>
            }
        </div>
    );
};

export default StatsPage;