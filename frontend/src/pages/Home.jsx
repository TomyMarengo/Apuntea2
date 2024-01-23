import { Trans, useTranslation } from 'react-i18next';
import { NavLink } from 'react-router-dom';
import { selectCurrentUser } from '../store/slices/authSlice';
import { UsersIcon, SearchAltIcon, AddDocumentIcon, UserIcon } from '../components/Icons';
import Card from '../components/Card';
import { useMemo } from 'react';
import { useSelector } from 'react-redux';

const Home = () => {
  const { t } = useTranslation();
  const user = useSelector(selectCurrentUser);

  let indexCards = useMemo(() => {
    return [
      {
        title: 'pages.index.cards.0.title',
        description: 'pages.index.cards.0.description',
        link: '/register',
        icon: <UsersIcon className="icon-index-card" />,
      },
      {
        title: 'pages.index.cards.1.title',
        description: 'pages.index.cards.1.description',
        link: '/search',
        icon: <SearchAltIcon className="icon-index-card" />,
      },
      {
        title: 'pages.index.cards.2.title',
        description: 'pages.index.cards.2.description',
        link: '/noteboard',
        icon: <AddDocumentIcon className="icon-index-card" />,
      },
      {
        title: 'pages.index.cards.3.title',
        description: 'pages.index.cards.3.description',
        link: '/profile',
        icon: <UserIcon className="icon-index-card" />,
      },
    ];
  }, []);

  let modifiedIndexCards = useMemo(() => {
    let cards = [...indexCards];
    if (user) {
      cards.splice(0, 1);
    } else {
      cards.splice(3, 1);
    }
    return cards;
  }, [user, indexCards]);

  return (
    <section className="flex flex-col items-center mt-16 gap-4 text-center">
      <h1 className="text-7xl text-dark-text">
        <Trans
          i18nKey="pages.index.title"
          values={{ pageName: t('apuntea') }}
          components={[<span key="0" className="text-pri font-bold"></span>]}
        />
      </h1>
      <h3 className="text-2xl text-dark-text">{t('pages.index.subtitle')}</h3>
      <div className="index-card-container">
        {modifiedIndexCards.map((card) => (
          <NavLink to={card.link} key={card.title} className="index-card">
            {card.icon}
            <h4 className="text-3xl text-dark-text">{t(card.title)}</h4>
            <p className="text-lg ">{t(card.description)}</p>
          </NavLink>
        ))}
      </div>
    </section>
  );
};

export default Home;
