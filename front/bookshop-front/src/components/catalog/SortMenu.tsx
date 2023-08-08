import React from 'react';
import {FormControl, MenuItem, Select, SelectChangeEvent} from '@mui/material';
import {Column, Order} from "../../enums/Sort";
import {useTranslation} from "react-i18next";

interface SortSelectProps {
    sortField: string;
    sortDirection: string;
    onSortChange: (value: string) => void;
}

const SortMenu: React.FC<SortSelectProps> = ({sortField, sortDirection, onSortChange}) => {
    const {t} = useTranslation();
    const handleSortChange = (event: SelectChangeEvent<`${string}:${string}`>) => {
        const value = event.target.value;
        onSortChange(value);
    };

    return (
        <FormControl>
            <Select value={`${sortField}:${sortDirection}`} onChange={handleSortChange}>
                <MenuItem value={`${Column.Id}:${Order.Asc}`}>{t('sortingOldest')}</MenuItem>
                <MenuItem value={`${Column.Id}:${Order.Desc}`}>{t('sortingNewest')}</MenuItem>
                <MenuItem value={`${Column.Title}:${Order.Asc}`}>{t('sortByTitle')}</MenuItem>
                <MenuItem value={`${Column.Author}:${Order.Asc}`}>{t('sortByAuthor')}</MenuItem>
            </Select>
        </FormControl>
    );
};

export default SortMenu;