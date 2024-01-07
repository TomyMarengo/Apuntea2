import type { Meta, StoryObj } from "@storybook/react";

import InputAutocomplete from "./InputAutocomplete";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "InputAutocomplete",
  component: InputAutocomplete,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof InputAutocomplete>;

export default meta;
type Story = StoryObj<typeof meta>;

const institutions = [
  { label: "ITBA", value: "ITBA" },
  { label: "UBA", value: "UBA" },
];

export const Base: Story = {
  args: {
    placeholder: "Enter your password",
    items: institutions,
  },
};
