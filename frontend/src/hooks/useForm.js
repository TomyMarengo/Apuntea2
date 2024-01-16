import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';

const useForm = ({ initialValues, submitCallback, dispatchCallback, redirectUrl, redirectErrorUrl }) => {
  const [form, setForm] = useState(initialValues);
  const [error, setError] = useState('');
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const newValue = type === 'checkbox' ? checked : value;
    setForm({ ...form, [name]: newValue });
  };

  const resetForm = () => {
    setForm(initialValues);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await submitCallback(form);
      if (dispatchCallback) dispatch(dispatchCallback({ ...response }));
      setForm(initialValues);
      setError('');
      if (redirectUrl) navigate(redirectUrl);
    } catch (error) {
      setError(error.message);
      toast.error(error.message);
      if (redirectErrorUrl) navigate(redirectErrorUrl);
    }
  };

  return { form, resetForm, error, handleChange, handleSubmit };
};

export default useForm;
