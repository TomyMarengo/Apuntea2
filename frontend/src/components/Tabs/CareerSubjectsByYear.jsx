import { DirectoryIconButton } from '../index';
import { useCareerSubjectsByYear } from '../../hooks';

const CareerSubjectsByYear = ({ careerId, year }) => {
  const { data: dataYear, isLoading: isLoadingYear } = useCareerSubjectsByYear(careerId, year);

  return (
    <>
      {isLoadingYear ? (
        <p>Loading...</p>
      ) : dataYear.length > 0 ? (
        dataYear.map((subject, index) => (
          <DirectoryIconButton key={index} name={subject.name} directoryId={subject.id} />
        ))
      ) : (
        <p>No data available</p>
      )}
    </>
  );
};

export default CareerSubjectsByYear;
