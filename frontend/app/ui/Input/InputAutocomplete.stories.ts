import type { Meta, StoryObj } from "@storybook/react";

import InputAutocomplete from "./InputAutocomplete";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "InputPassword",
  component: InputAutocomplete,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof InputAutocomplete>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Autocomplete: Story = {
  args: {},
};
