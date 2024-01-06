import Link from "next/link";
import Button from "../Button/Button";

const Navbar = () => {
  return (
    <nav className="min-h-[60px] flex justify-between bg-dark-bg items-center px-4">
      <Link href="/" className="text-dark-primary font-black text-4xl">
        Apuntea
      </Link>
      <div className="flex items-center gap-4">
        <Button href="/login">Log In</Button>
        <Button href="/signup" outlined>
          Sign up
        </Button>
      </div>
    </nav>
  );
};

export default Navbar;
