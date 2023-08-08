import React from 'react';
import { useTranslation } from 'react-i18next';

const LanguageSwitcher: React.FC = () => {
    const { i18n } = useTranslation();

    return (
        <div>
            <button onClick={() => i18n.changeLanguage('english')}>English</button>
            <button onClick={() => i18n.changeLanguage('russian')}>Русский</button>
        </div>
    );
};

export default LanguageSwitcher;