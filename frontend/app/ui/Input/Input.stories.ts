import type { Meta, StoryObj } from "@storybook/react";

import Input from "./Input";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "Input/Input",
  component: Input,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof Input>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Base: Story = {
  args: {
    type: "text",
    placeholder: "Write here",
  },
};

export const Erasable: Story = {
  args: {
    type: "text",
    placeholder: "Write here",
    erasable: true,
  },
};
