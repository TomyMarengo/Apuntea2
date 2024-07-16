import { useState } from 'react';
import { TabContent } from '../index';

const TabbedCard = ({ tabs }) => {
  const [activeTab, setActiveTab] = useState(0);

  const handleTabClick = (tabIndex) => {
    setActiveTab(tabIndex);
  };

  return (
    <div className="w-full px-14">
      <div className="mini-nav">
        {tabs.map((tab, index) => (
          <button
            key={index}
            onClick={() => handleTabClick(index)}
            className={`tab ${activeTab === index ? 'active' : ''}`}
          >
            {tab.name}
          </button>
        ))}
      </div>

      {tabs.map((tab, index) => (
        <TabContent key={index} active={activeTab === index}>
          {tab.content}
        </TabContent>
      ))}
    </div>
  );
};

export default TabbedCard;
