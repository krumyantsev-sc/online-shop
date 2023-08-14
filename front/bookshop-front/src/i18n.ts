import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

i18n
    .use(initReactI18next)
    .init({
        fallbackLng: 'english',
        interpolation: {
            escapeValue: false,
        },
        resources: {
            english: {
                translation: require('./L5zero/out/en.json'),
            },
            russian: {
                translation: require('./L5zero/out/ru.json'),
            }
        },
    });

export default i18n;