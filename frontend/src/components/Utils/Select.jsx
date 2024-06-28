import { useTranslation } from 'react-i18next';
import { useState, useEffect } from 'react';

const Select = ({ options, defaultValue, onChange, errors, name }) => {
  const { t } = useTranslation();

  const [selectedOption, setSelectedOption] = useState(defaultValue);

  useEffect(() => {
    setSelectedOption(defaultValue);
  }, [defaultValue]);

  const handleChange = (e) => {
    const selectedValue = e.target.value;
    setSelectedOption(selectedValue);

    if (onChange) {
      onChange({ target: { name, value: selectedValue, type: 'select' } });
    }
  };

  return (
    <>
      <select onChange={handleChange} value={selectedOption}>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {errors?.content &&
        errors.content.length > 0 &&
        errors.content.map((error) => (
          <span className="text-red-500" key={error}>
            {t(error)}
          </span>
        ))}
    </>
  );
};

export default Select;
