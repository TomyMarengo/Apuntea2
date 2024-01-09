import type { Meta, StoryObj } from "@storybook/react";
import DirectoryItem from "./DirectoryItem";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "Icon/DirectoryItem",
  component: DirectoryItem,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof DirectoryItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Base: Story = {
  args: {
    name: "My Directory",
    self: ".",
  },
};
