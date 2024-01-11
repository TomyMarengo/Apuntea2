import { useGetInstitutionsQuery } from '../store/slices/institutionsApiSlice';

const Profile = () => {
  const { data: institutions, isLoading, error } = useGetInstitutionsQuery();

  return isLoading ? (
    <h1>Loading...</h1>
  ) : (
    <div className="flex flex-col gap-4">
      <h1 className="text-2xl">Profile</h1>
      <p className="text-xl">Institutions</p>
      <ul className="flex flex-col gap-2">
        {institutions.map((institution) => (
          <li key={institution.name}>{institution.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default Profile;
