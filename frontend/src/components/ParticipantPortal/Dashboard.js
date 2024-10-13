import React, { useState, useEffect } from 'react';
import { Box, Heading, VStack, HStack, Text, Button, useColorModeValue } from '@chakra-ui/react';
import axios from 'axios';

const Dashboard = () => {
  const [pendingAgreements, setPendingAgreements] = useState([]);
  const [recentAgreements, setRecentAgreements] = useState([]);
  const bgColor = useColorModeValue('gray.100', 'gray.700');

  useEffect(() => {
    // Fetch pending and recent agreements
    const fetchAgreements = async () => {
      try {
        const pendingResponse = await axios.get('/api/agreements?status=pending');
        setPendingAgreements(pendingResponse.data);

        const recentResponse = await axios.get('/api/agreements?limit=5');
        setRecentAgreements(recentResponse.data);
      } catch (error) {
        console.error('Error fetching agreements:', error);
      }
    };

    fetchAgreements();
  }, []);

  return (
    <Box p={5}>
      <Heading mb={6}>Participant Dashboard</Heading>
      <HStack spacing={8} align="start">
        <VStack align="stretch" flex={1} spacing={4}>
          <Heading size="md">Pending Agreements</Heading>
          {pendingAgreements.map((agreement) => (
            <Box key={agreement.id} p={3} bg={bgColor} borderRadius="md">
              <Text fontWeight="bold">{agreement.name}</Text>
              <Text fontSize="sm">Status: {agreement.status}</Text>
              <Button size="sm" colorScheme="blue" mt={2}>
                View Details
              </Button>
            </Box>
          ))}
        </VStack>
        <VStack align="stretch" flex={1} spacing={4}>
          <Heading size="md">Recent Agreements</Heading>
          {recentAgreements.map((agreement) => (
            <Box key={agreement.id} p={3} bg={bgColor} borderRadius="md">
              <Text fontWeight="bold">{agreement.name}</Text>
              <Text fontSize="sm">Status: {agreement.status}</Text>
              <Button size="sm" colorScheme="green" mt={2}>
                View Agreement
              </Button>
            </Box>
          ))}
        </VStack>
      </HStack>
    </Box>
  );
};

export default Dashboard;
