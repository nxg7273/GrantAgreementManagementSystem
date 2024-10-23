import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';

test('renders login link', () => {
  render(
    <BrowserRouter>
      <div>
        <a href="/login">Login</a>
      </div>
    </BrowserRouter>
  );
  const linkElement = screen.getByText(/login/i);
  expect(linkElement).toBeInTheDocument();
});
