import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { debounce } from 'lodash';

const useForm = ({ args, initialValues, submitCallback, dispatchCallback, schema, redirectUrl, redirectErrorUrl }) => {
  const [form, setForm] = useState(initialValues);
  const [message, setMessage] = useState('');
  const [errors, setErrors] = useState({});
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const newValue = type === 'checkbox' ? checked : value;
    if (schema) {
      const validatedField = schema.pick({ [name]: true }).safeParse({ [name]: newValue });
      if (!validatedField.success) {
        const debouncedSetErrors = debounce((errors) => setErrors(errors), 250);
        debouncedSetErrors({ ...errors, ...validatedField.error.flatten().fieldErrors });
      }
      setErrors({ ...errors, [name]: '' });
    }
    setForm((prevForm) => ({ ...prevForm, [name]: newValue }));
  };

  const setFormValues = (currentForm) => {
    setForm({ ...currentForm });
  };

  const resetForm = () => {
    setForm(initialValues);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (schema) {
        const validatedFields = schema.safeParse(form);
        if (!validatedFields.success) {
          setErrors(validatedFields.error.flatten().fieldErrors);
          // throw new Error("Form doesn't match schema");
        }
      }
      const response = await submitCallback({ ...args, ...form });
      if (dispatchCallback) dispatch(dispatchCallback({ ...response }));
      setMessage('');
      setErrors({});
      if (redirectUrl) navigate(redirectUrl);
    } catch (error) {
      setMessage(error.message);
      toast.error(error.message);
      if (redirectErrorUrl) navigate(redirectErrorUrl);
    } /*finally {
      setForm(initialValues);
    }*/
  };

  return { form, setFormValues, resetForm, message, errors, handleChange, handleSubmit };
};

export default useForm;
