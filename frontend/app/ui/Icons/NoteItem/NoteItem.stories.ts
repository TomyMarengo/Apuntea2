import type { Meta, StoryObj } from "@storybook/react";
import { FileType } from "../../../lib/definitions";
import NoteItem from "./NoteItem";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
  title: "Icon/NoteItem",
  component: NoteItem,
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof NoteItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Base: Story = {
  args: {
    fileType: FileType.PDF,
    name: "My Note",
    self: ".",
  },
};
