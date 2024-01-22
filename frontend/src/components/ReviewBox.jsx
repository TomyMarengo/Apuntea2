import { Box, Button } from './index';

const ReviewBox = () => {
  return (
    <Box className="flex flex-col">
      <div className="my-2">
        <textarea
          className="review-text bg-dark-bg w-full p-2 border-[1px] background-bg resize-none rounded-lg focus:ring-4 focus:ring-light-pri"
          placeholder="Escribe un comentario..."
        ></textarea>
        <div className="flex flex-row justify-between mt-3 gap-4 ">
          <select className="w-full border-[1px] rounded-xl p-2 focus:ring-4 focus:ring-light-pri">
            <option value="5">⭐⭐⭐⭐⭐</option>
            <option value="4">⭐⭐⭐⭐</option>
            <option value="3">⭐⭐⭐</option>
            <option value="2">⭐⭐</option>
            <option value="1">⭐</option>
          </select>
          <Button>Enviar</Button>
        </div>
      </div>
    </Box>
  );
};

export default ReviewBox;
