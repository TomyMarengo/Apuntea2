import clsx from "clsx";
import styles from "./Button.module.css";
import Link from "next/link";
import { useMemo, ButtonHTMLAttributes, AnchorHTMLAttributes } from "react";

type ButtonPropsCommon = {
  children: React.ReactNode;
  primary?: boolean;
  outlined?: boolean;
};

type ButtonProps =
  | (ButtonPropsCommon & ButtonHTMLAttributes<HTMLButtonElement>)
  | (ButtonPropsCommon & {
      href: string;
    } & AnchorHTMLAttributes<HTMLAnchorElement>);

const Button = ({
  primary = true,
  outlined = false,
  children,
  ...props
}: ButtonProps) => {
  const classes = useMemo(
    () =>
      clsx(
        outlined && "border bg-opacity-0",
        primary ? "bg-primary border-primary" : "bg-secondary border-secondary",
        outlined && (primary ? "text-primary" : "text-secondary"),
        { "text-white": !outlined },
        styles.button,
        props.className
      ),
    [outlined, primary, props.className]
  );

  if ("href" in props) {
    const { href, ...anchorProps } = props;
    return (
      <Link href={href} className={classes} {...anchorProps}>
        {children}
      </Link>
    );
  } else {
    return (
      <button type="button" className={classes} {...props}>
        {children}
      </button>
    );
  }
};

export default Button;
