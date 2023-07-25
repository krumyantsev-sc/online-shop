import React from 'react';
import {FormControl, MenuItem, Select, SelectChangeEvent} from '@mui/material';
import {Column, Order} from "../../enums/Sort";

interface SortSelectProps {
    sortField: string;
    sortDirection: string;
    onSortChange: (value: string) => void;
}

const SortMenu: React.FC<SortSelectProps> = ({sortField, sortDirection, onSortChange}) => {
    const handleSortChange = (event: SelectChangeEvent<`${string}:${string}`>) => {
        const value = event.target.value;
        onSortChange(value);
    };

    return (
        <FormControl>
            <Select value={`${sortField}:${sortDirection}`} onChange={handleSortChange}>
                <MenuItem value={`${Column.Id}:${Order.Asc}`}>Сначала старые</MenuItem>
                <MenuItem value={`${Column.Id}:${Order.Desc}`}>Сначала новые</MenuItem>
                <MenuItem value={`${Column.Title}:${Order.Asc}`}>По названию</MenuItem>
                <MenuItem value={`${Column.Author}:${Order.Asc}`}>По автору</MenuItem>
            </Select>
        </FormControl>
    );
};

export default SortMenu;