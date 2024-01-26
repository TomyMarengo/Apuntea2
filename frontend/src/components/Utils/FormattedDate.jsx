const FormattedDate = ({ date }) => {
  const formattedDate = new Date(date);
  /* const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
    timeZoneName: 'short',
  }; */

  const options = {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  };

  const formattedDateTime = new Intl.DateTimeFormat(navigator.language, options).format(formattedDate);

  return <>{formattedDateTime}</>;
};

export default FormattedDate;
