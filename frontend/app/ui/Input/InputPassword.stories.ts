import type { Meta, StoryObj } from "@storybook/react";

import InputPassword from "./InputPassword";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "Input/InputPassword",
  component: InputPassword,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof InputPassword>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Base: Story = {
  args: {
    placeholder: "Enter your password",
  },
};
